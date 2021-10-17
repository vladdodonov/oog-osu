package com.dodonov.oogosu.mapstruct;

import org.mapstruct.Mapper;

@Mapper//(uses = StatusDocumentPackageDtoMapper.class)
public interface DocumentPackageForNpaDtoMapper {
/*
    DocumentPackageForNpaDtoMapper INSTANCE = Mappers.getMapper(DocumentPackageForNpaDtoMapper.class);

    @Mapping(target = "documentPackageNumber", source = "number")
    @Mapping(target = "documentPackageId", source = "id")
    @Mapping(target = "documentPackageStatuses", source = "packageDocument", qualifiedByName = "mapStatusesToDto")
    DocumentPackageForNpaDto toDto(PackageDocument packageDocument);

    @Mapping(target = "number", source = "documentPackageNumber")
    @Mapping(target = "id", source = "documentPackageId")
    @Mapping(target = "documentPackageStatuses", source = "documentPackageDto", qualifiedByName = "mapStatusesToEntity")
    PackageDocument toEntity(DocumentPackageForNpaDto documentPackageDto);

    @Named("mapStatusesToEntity")
    default Set<StatusPackageDocument> mapStatusesToEntity(DocumentPackageForNpaDto documentPackageDto) {
        var statusMapper = Mappers.getMapper(StatusDocumentPackageDtoMapper.class);
        if (isNotEmpty(documentPackageDto.getDocumentPackageStatuses())) {

            return documentPackageDto.getDocumentPackageStatuses().stream()
                    .map(statusMapper::toEntity)
                    .collect(Collectors.toSet());
        }
        return null;
    }

    @Named("mapStatusesToDto")
    default List<DocumentPackageStatusDto> mapStatusesToDto(PackageDocument packageDocument) {
        var statusMapper = Mappers.getMapper(StatusDocumentPackageDtoMapper.class);
        if (isNotEmpty(packageDocument.getDocumentPackageStatuses())) {

            return packageDocument.getDocumentPackageStatuses().stream()
                    .map(statusMapper::toDto)
                    .collect(Collectors.toList());
        }
        return null;
    }*/
}
