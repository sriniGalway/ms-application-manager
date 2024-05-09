package uk.gov.dwp.health.pip.application.manager.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uk.gov.dwp.health.pip.application.manager.model.registration.data.AboutYourHealthSchema110;
import uk.gov.dwp.health.pip.application.manager.model.registration.data.HealthProfessionalsDetailsSchema100;
import uk.gov.dwp.health.pip.application.manager.model.registration.data.HospitalHospiceOrCarehomeSchema100;
import uk.gov.dwp.health.pip.application.manager.openapi.registration.v3.dto.AboutYourHealthDto;
import uk.gov.dwp.health.pip.application.manager.openapi.registration.v3.dto.CareAccommodationDto;
import uk.gov.dwp.health.pip.application.manager.openapi.registration.v3.dto.HealthProfessionalDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class AboutYourHealthMapperV3 {

  private final AddressMapperV3 addressMapperV3;

  AboutYourHealthDto toDto(AboutYourHealthSchema110 aboutYourHealth) {
    HospitalHospiceOrCarehomeSchema100 hospitalHospiceOrCarehome =
        aboutYourHealth.getHospitalHospiceOrCarehome();
    List<HealthProfessionalsDetailsSchema100> healthProfessionalsDetailsList =
        aboutYourHealth.getHealthProfessionalsDetails();

    return new AboutYourHealthDto()
            .conditions(aboutYourHealth.getHealthConditions())
            .careAccommodation(
                    new CareAccommodationDto()
                            .accommodationType(getAccommodationType(hospitalHospiceOrCarehome))
                            .admissionDate(getAdmissionDate(hospitalHospiceOrCarehome))
                            .address(addressMapperV3.getAddress(hospitalHospiceOrCarehome)))
            .hcpContactConsent(aboutYourHealth.getHcpContactConsent())
            .hcpShareConsent(aboutYourHealth.getHcpShareConsent())
            .healthProfessionals(getHealthProfessionals(healthProfessionalsDetailsList));
  }

  private List<HealthProfessionalDto> getHealthProfessionals(
          List<HealthProfessionalsDetailsSchema100> healthProfessionalsDetailsList) {
    return healthProfessionalsDetailsList.stream()
            .map(this::toHealthProfessionalDto)
            .collect(Collectors.toList());
  }

  private HealthProfessionalDto toHealthProfessionalDto(
          HealthProfessionalsDetailsSchema100 healthProfessionalsDetails) {
    return new HealthProfessionalDto()
            .name(healthProfessionalsDetails.getName())
            .profession(healthProfessionalsDetails.getProfession())
            .phoneNumber(healthProfessionalsDetails.getPhoneNumber())
            .address(addressMapperV3.getAddress(healthProfessionalsDetails.getAddress()))
            .lastContact(healthProfessionalsDetails.getLastContact());
  }

  private CareAccommodationDto.AccommodationTypeEnum getAccommodationType(
          HospitalHospiceOrCarehomeSchema100 hospitalHospiceOrCarehome) {
    var accommodationTypeValue = hospitalHospiceOrCarehome.getAccommodationType().value();
    return CareAccommodationDto.AccommodationTypeEnum.fromValue(accommodationTypeValue);
  }

  private String getAdmissionDate(HospitalHospiceOrCarehomeSchema100 hospitalHospiceOrCarehome) {
    return (String) hospitalHospiceOrCarehome.getAdditionalProperties().get("admissionDate");
  }
}
