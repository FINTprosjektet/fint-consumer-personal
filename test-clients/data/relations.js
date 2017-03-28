module.exports = {
    corrId: '',
    action: 'GET_ALL_RELATIONS',
    status: 'UPSTREAM_QUEUE',
    time: new Date().getTime(),
    orgId: '',
    source: 'fk',
    client: 'vfs',
    message: null,
    data: [
        {
            type: 'urn:fint.no:person:personalressurs:person.fodselsnummer:personalressurs.ansattnummer',
            main: '204194497763',
            related: '10025'
        },
        {
            type: 'urn:fint.no:personalressurs:person:personalressurs.ansattnummer:person.fodselsnummer',
            main: '10025',
            related: '204194497763'
        },
        {
            type: 'urn:fint.no:arbeidsforhold:personalressurs:arbeidsforhold.systemid:personalressurs.ansattnummer',
            main: '6db6fcd3-3edc-43b4-b0d4-07adb71d486e',
            related: '10025'
        },
        {
            type: 'urn:fint.no:personalressurs:arbeidsforhold:personalressurs.ansattnummer:arbeidsforhold.systemid',
            main: '10025',
            related: '6db6fcd3-3edc-43b4-b0d4-07adb71d486e'
        }
    ]
}