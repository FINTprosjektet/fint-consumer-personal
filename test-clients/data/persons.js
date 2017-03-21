module.exports = {
    corrId: '',
    action: 'GET_ALL_PERSONS',
    status: 'UPSTREAM_QUEUE',
    time: new Date().getTime(),
    orgId: '',
    source: 'fk',
    client: 'vfs',
    message: null,
    data:
    [
        {
            kontaktinformasjon: {
                epostadresse: 'ola@flytt.no',
                telefonnummer: '57322',
                mobiltelefonummer: '43746353',
                nettsted: 'http://www.faring.no'
            },
            postadresse: {
                adresse: 'Veien 92',
                postnummer: '5500',
                poststed: 'Haugesund',
                land: {
                    kode: 'NO'
                }
            },
            foedselsnummer: {
                identifikatorverdi: '204194497763'
            },
            navn: {
                fornavn: 'Ola',
                etternavn: 'Flytt',
                mellomnavn: 'Åre'
            },
            kjoenn: {
                kode: '1',
                beskrivelse: 'mann'
            },
            foedselsdato: 415321200000,
            statsborgerskap: {
                kode: 'NO'
            },
            bostedsadresse: {
                adresse: 'Veien 92',
                postnummer: '5500',
                poststed: 'Haugesund',
                land: {
                    kode: 'NO'
                }
            }
        }
    ]
}