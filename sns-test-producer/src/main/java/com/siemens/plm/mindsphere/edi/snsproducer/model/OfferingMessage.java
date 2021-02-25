package com.siemens.plm.mindsphere.edi.snsproducer.model;

import com.siemens.plm.mindsphere.edi.messaging.model.EdiMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class OfferingMessage implements EdiMessage {

	private String offeringId;
	private String tenantId;
	private String traceId;
	@Override
	public String toJson() {
		return EdiMessage.GSON.toJson(this);
	}

	@Override
	public String toString() {
		return toJson();
	}
}
