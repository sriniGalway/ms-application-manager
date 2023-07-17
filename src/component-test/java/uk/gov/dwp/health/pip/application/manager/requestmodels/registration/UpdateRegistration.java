package uk.gov.dwp.health.pip.application.manager.requestmodels.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import uk.gov.dwp.health.pip.application.manager.requestmodels.Meta;

@Getter
@Builder(toBuilder = true)
public class UpdateRegistration {

  @JsonProperty("form_data")
  @Default private final FormData formData = FormData.builder().build();

  @Default private final Meta meta = Meta.builder().build();
}
