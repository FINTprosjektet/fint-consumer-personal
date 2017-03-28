module.exports = {
    corrId: '',
    action: 'GET_ALL_EMPLOYEES',
    status: 'UPSTREAM_QUEUE',
    time: new Date().getTime(),
    orgId: '',
    source: 'fk',
    client: 'vfs',
    message: null,
    data: [
        {
            brukernavn: {
                identifikatorverdi: 'u10025'
            },
            systemId: {
                identifikatorverdi: 'a5ac4795-f14e-4973-b388-695b2ec3b4d6'
            },
            ansattnummer: {
                identifikatorverdi: '10025'
            },
            ansettelsesperiode: {
                start: 415321200000,
                slutt: 415321200000
            },
            kontaktinformasjon: {
                epostadresse: 'ola.flytt@mock.no',
                telefonnummer: '48972723',
                mobiltelefonummer: '38933079',
                nettsted: 'http://www.mock.no'
            }
        }
    ]
}