module.exports = {
    corrId: '',
    action: 'GET_ALL_EMPLOYMENTS',
    status: 'UPSTREAM_QUEUE',
    time: new Date().getTime(),
    orgId: '',
    source: 'fk',
    client: 'vfs',
    message: null,
    data:
    [
        {
            ansattnummer: {
                identifikatorverdi: '10001'
            },
            systemId: {
                identifikatorverdi: '6db6fcd3-3edc-43b4-b0d4-07adb71d486e'
            },
            arbeidforholdstype: {
                kode: 'A',
                navn: 'Fast'
            },
            arbeidsforholdsperiode: {
                start: 415321200000,
                slutt: 415321200000
            },
            erAktiv: true,
            aarsloenn: 0.5743517259482673,
            stillingstittel: 'Lønnsmedarbeider',
            stillingskode: {
                kode: '753100',
                navn: 'Saksbehandler',
                ksKode: '7531'
            },
            ansettelsesprosent: 100,
            loennsprosent: 100,
            stillingsnummer: '1ff37755-6d6f-4245-be12-855137b88d4e',
            hovedstilling: true,
            ansvar: {
                kode: '10',
                navn: 'ansvaret'
            },
            funksjon: {
                kode: '20',
                navn: 'funksjon'
            },
            timerPerUke: {
                kode: '37,5',
                navn: '37,5 timers uke'
            }
        }
    ]
}