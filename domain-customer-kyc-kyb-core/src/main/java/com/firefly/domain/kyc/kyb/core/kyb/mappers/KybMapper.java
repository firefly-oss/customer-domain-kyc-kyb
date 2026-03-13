package com.firefly.domain.kyc.kyb.core.kyb.mappers;

import com.firefly.core.kycb.sdk.model.CorporateDocumentDTO;
import com.firefly.core.kycb.sdk.model.UboDTO;
import com.firefly.domain.kyc.kyb.core.kyb.commands.DocumentData;
import com.firefly.domain.kyc.kyb.core.kyb.commands.UboData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper between domain value objects and SDK DTOs.
 *
 * <p>{@code partyId} is intentionally ignored in both mappings — it is
 * a required field on the DTOs that comes from the command context, not
 * from the value object itself. Handlers set it via the fluent setter
 * after calling the mapper.</p>
 *
 * <p>Read-only DTO constructor fields ({@code dateCreated}, {@code dateUpdated},
 * and the ID field) are set to {@code null} on creation — the core service
 * assigns them server-side.</p>
 */
@Mapper(componentModel = "spring")
public interface KybMapper {

    /**
     * Maps a {@link DocumentData} value object to a {@link CorporateDocumentDTO}.
     * The caller must set {@code partyId} on the returned DTO before the SDK call.
     */
    @Mapping(target = "partyId", ignore = true)
    @Mapping(target = "isVerified", constant = "false")
    CorporateDocumentDTO toDto(DocumentData data);

    /**
     * Maps a {@link UboData} value object to a {@link UboDTO}.
     * The caller must set {@code partyId} on the returned DTO before the SDK call.
     * If {@code startDate} is null, defaults to current timestamp.
     */
    @Mapping(target = "partyId", ignore = true)
    @Mapping(target = "isVerified", constant = "false")
    @Mapping(target = "startDate",
             expression = "java(uboData.getStartDate() != null ? uboData.getStartDate() : java.time.LocalDateTime.now())")
    UboDTO toDto(UboData uboData);
}
