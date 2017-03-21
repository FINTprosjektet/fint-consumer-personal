module.exports = {
    corrId: '',
    action: 'GET_RELATIONS',
    status: 'UPSTREAM_QUEUE',
    time: new Date().getTime(),
    orgId: '',
    source: 'fk',
    client: 'vfs',
    message: null,
    data: [
        {
            type: 'https://api.felleskomponent.no/rel/person.foedselsnummer.identifikatorverdi:personalressurs.ansattnummer.identifikatorverdi',
            leftKey: '204194497763',
            rightKey: '10001'
        },
        {
            type: 'https://api.felleskomponent.no/rel/personalressurs.ansattnummer.identifikatorverdi:person.foedselsnummer.identifikatorverdi',
            leftKey: '10001',
            rightKey: '204194497763'
        },
        {
            type: 'https://api.felleskomponent.no/rel/arbeidsforhold.stillingsnummer:personalressurs.ansattnummer.identifikatorverdi',
            leftKey: '1ff37755-6d6f-4245-be12-855137b88d4e',
            rightKey: '10001'
        },
        {
            type: 'https://api.felleskomponent.no/rel/personalressurs.ansattnummer.identifikatorverdi:arbeidsforhold.stillingsnummer',
            leftKey: '10001',
            rightKey: '1ff37755-6d6f-4245-be12-855137b88d4e'
        }
    ]
}