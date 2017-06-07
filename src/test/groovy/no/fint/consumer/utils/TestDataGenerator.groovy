package no.fint.consumer.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import no.fint.model.administrasjon.personal.Arbeidsforhold
import no.fint.model.administrasjon.personal.Personalressurs
import no.fint.model.felles.Identifikator
import no.fint.model.relation.FintResource
import no.fint.model.relation.Relation

class TestDataGenerator {

    static void main(String[] args) {
        def objectMapper = new ObjectMapper()
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)

        Relation relation = new Relation.Builder()
                .with(Personalressurs.Relasjonsnavn.PERSON)
                .forType(Arbeidsforhold)
                .field('ansattnummer')
                .value('10025').build()

        Identifikator identifikator = new Identifikator(identifikatorverdi: '6db6fcd3-3edc-43b4-b0d4-07adb71d486e')
        Arbeidsforhold arbeidsforhold = new Arbeidsforhold(systemId: identifikator)

        FintResource fintResource = FintResource.with(arbeidsforhold).addRelasjoner(relation)

        def json = objectMapper.writeValueAsString(fintResource)

        json != null
        println json
    }
}
