package com.dodonov.oogosu.service.impl;

import com.dodonov.oogosu.domain.enums.ColorOnAverage;
import com.dodonov.oogosu.domain.enums.Decision;
import com.dodonov.oogosu.dto.DepartmentData;
import com.dodonov.oogosu.dto.ReportDto;
import com.dodonov.oogosu.repository.AppealRepository;
import com.dodonov.oogosu.service.ReportService;
import com.dodonov.oogosu.utils.transformer.LongIdCollectionFieldResultTransformer;
import com.dodonov.oogosu.utils.transformer.RowData;
import lombok.RequiredArgsConstructor;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.DoubleStream;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final EntityManager entityManager;

    public static ResultTransformer transformer() {
        return new DepartmentDataResultTransformer();
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDto getReport() {
        var departmentData = (List<DepartmentData>) entityManager.createNativeQuery("" +
                "with departments as ( " +
                "    select dep.id as depId, dep.name as depName " +
                "    from d_department dep " +
                "    where dep.archived is not true " +
                "), " +
                "     all_cnt as ( " +
                "         select count(*) as appCnt, dep.depId as depId " +
                "         from appeal a " +
                "                  left join departments dep on a.department_id = dep.depId " +
                "         group by depId " +
                "     ), " +
                "     prolonged_cnt as ( " +
                "         select sum(case when a.department_id is not null and a.is_prolonged is true then 1 else 0 end) as prolongedCnt, dep.depId as depId " +
                "         from departments dep " +
                "                  left join appeal a on a.department_id = dep.depId " +
                "         group by depId " +
                "     ), " +
                "     complaints_cnt as ( " +
                "         select sum(case when a.department_id is not null and a.is_complaint is true and a.decision = :positive then 1 else 0 end) as complaintsCnt, dep.depId as depId " +
                "         from departments dep " +
                "                  left join appeal a on a.department_id = dep.depId " +
                "         group by depId " +
                "     ), " +
                "     returned_cnt as ( " +
                "         select sum(case when a.department_id is not null and a.is_returned is true then 1 else 0 end) as returnedCnt, dep.depId as depId " +
                "         from departments dep " +
                "                  left join appeal a on a.department_id = dep.depId " +
                "         group by depId " +
                "     ) " +
                " " +
                "select dep.depId                                              as depId, " +
                "       dep.depName                                            as depName, " +
                "       all_cnt.appCnt                                         as appCnt, " +
                "       prolonged_cnt.prolongedCnt * 100 / all_cnt.appCnt      as prolongedCnt, " +
                "       complaints_cnt.complaintsCnt * 100 / all_cnt.appCnt    as complaintsCnt, " +
                "       returned_cnt.returnedCnt * 100 / all_cnt.appCnt        as returnedCnt " +
                "from departments dep " +
                "         left join all_cnt on dep.depId = all_cnt.depId " +
                "         left join prolonged_cnt on dep.depId = prolonged_cnt.depId " +
                "         left join complaints_cnt on dep.depId = complaints_cnt.depId " +
                "         left join returned_cnt on dep.depId = returned_cnt.depId")
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(transformer())
                .setParameter("positive", Decision.POSITIVE.name())
                .getResultList();
        var dto = new ReportDto();
        dto.setDepartmentDataList(departmentData);
        setNumberOfAppealsNormalized(dto);
        setProlongedPercentNormalized(dto);
        setSubstantiatedComplaintsPercentNormalized(dto);
        setReturnedOnRevisionPercentNormalized(dto);
        setCriteria(dto);
        setAverageCriteria(dto);
        setColorOnAverage(dto);
        return dto;
    }

    private void setNumberOfAppealsNormalized(ReportDto reportDto) {
        var max = getMaxValue(reportDto.getDepartmentDataList().stream()
                .mapToDouble(DepartmentData::getNumberOfAppeals));
        reportDto.getDepartmentDataList()
                .forEach(dd -> {
                    dd.setNumberOfAppealsNormalized(roundingNormalizedValue(dd.getNumberOfAppeals() / max));
                });
    }

    private void setProlongedPercentNormalized(ReportDto reportDto) {
        var max = getMaxValue(reportDto.getDepartmentDataList().stream()
                .mapToDouble(DepartmentData::getProlongedPercent));
        reportDto.getDepartmentDataList()
                .forEach(dd -> {
                    dd.setProlongedPercentNormalized(roundingNormalizedValue(dd.getProlongedPercent() / max));
                });
    }

    private void setSubstantiatedComplaintsPercentNormalized(ReportDto reportDto) {
        var max = getMaxValue(reportDto.getDepartmentDataList().stream()
                .mapToDouble(DepartmentData::getSubstantiatedComplaintsPercent));
        reportDto.getDepartmentDataList()
                .forEach(dd -> {
                    dd.setSubstantiatedComplaintsPercentNormalized(roundingNormalizedValue(dd.getSubstantiatedComplaintsPercent() / max));
                });
    }

    private void setReturnedOnRevisionPercentNormalized(ReportDto reportDto) {
        var max = getMaxValue(reportDto.getDepartmentDataList().stream()
                .mapToDouble(DepartmentData::getReturnedOnRevisionPercent));
        reportDto.getDepartmentDataList()
                .forEach(dd -> {
                    dd.setReturnedOnRevisionPercentNormalized(roundingNormalizedValue(dd.getReturnedOnRevisionPercent() / max));
                });
    }

    private void setCriteria(ReportDto reportDto) {
        reportDto.getDepartmentDataList()
                .forEach(dd -> {
                    var criteria = dd.getNumberOfAppealsNormalized() +
                            dd.getProlongedPercentNormalized() +
                            dd.getReturnedOnRevisionPercentNormalized() +
                            dd.getSubstantiatedComplaintsPercentNormalized();
                    dd.setCriteria(roundingNormalizedValue(criteria));
                });
    }

    private void setAverageCriteria(ReportDto reportDto) {
        var bd = BigDecimal.valueOf(reportDto.getDepartmentDataList().stream()
                .mapToDouble(DepartmentData::getCriteria)
                .summaryStatistics()
                .getAverage());
        reportDto.setAverageCriteria(roundingNormalizedValue(bd.doubleValue()));
    }

    private void setColorOnAverage(ReportDto reportDto) {
        reportDto.getDepartmentDataList()
                .forEach(dd -> {
                    dd.setColor(dd.getCriteria() <= reportDto.getAverageCriteria()
                            ? ColorOnAverage.GREEN
                            : ColorOnAverage.RED);
                });
    }

    private Double getMaxValue(DoubleStream d) {
        var res = d.summaryStatistics().getMax();
        return res == 0.0
                ? 1.0
                : res;
    }

    private Double roundingNormalizedValue(Double val) {
        var bd = BigDecimal.valueOf(val);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private static class DepartmentDataResultTransformer extends LongIdCollectionFieldResultTransformer<DepartmentData> {
        @Override
        protected DepartmentData createInstance(RowData rowData) {
            var data = new DepartmentData();
            data.setId(rowData.getLong("depId"));
            data.setName(rowData.getString("depName"));
            data.setNumberOfAppeals(rowData.getLong("appCnt"));
            data.setProlongedPercent(rowData.getDouble("prolongedCnt"));
            data.setSubstantiatedComplaintsPercent(rowData.getDouble("complaintsCnt"));
            data.setReturnedOnRevisionPercent(rowData.getDouble("returnedCnt"));
            return data;
        }

        @Override
        protected void fillCollections(DepartmentData result, RowData rowData) {

        }
    }
}
