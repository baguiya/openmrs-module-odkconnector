/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.odkconnector.serialization.serializer.openmrs;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PersonName;
import org.openmrs.annotation.Handler;
import org.openmrs.module.odkconnector.serialization.Serializer;

@Handler(supports = Patient.class, order = 50)
public class PatientSerializer implements Serializer {

	private static final Log log = LogFactory.getLog(PatientSerializer.class);

	/**
	 * Write the patient information to the output stream.
	 *
	 * @param stream the output stream
	 * @param data   the data that need to be written to the output stream
	 */
	@Override
	public void write(final OutputStream stream, final Object data) throws IOException {
        try {
            Patient patient = (Patient) data;

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

            DataOutputStream outputStream = new DataOutputStream(stream);
            // skip if the patient is an invalid patient
            if (patient == null || patient.getPersonName() == null || patient.getPatientIdentifier() == null)
                return;

            outputStream.writeInt(patient.getPatientId());

            PersonName personName = patient.getPersonName();
            outputStream.writeUTF(StringUtils.defaultString(personName.getFamilyName()));
            outputStream.writeUTF(StringUtils.defaultString(personName.getMiddleName()));
            outputStream.writeUTF(StringUtils.defaultString(personName.getGivenName()));

            outputStream.writeUTF(StringUtils.defaultString(patient.getGender()));

            Date birthDate = patient.getBirthdate();
            outputStream.writeUTF(birthDate != null ? dateFormat.format(birthDate.getTime()) : StringUtils.EMPTY);

            PatientIdentifier patientIdentifier = patient.getPatientIdentifier();
            outputStream.writeUTF(StringUtils.defaultString(patientIdentifier.getIdentifier()));
        } catch (IOException e) {
            log.info("Writing patient information failed!", e);
        }
    }
}
