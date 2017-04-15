module.exports = {
  corrId: '',
  action: 'GET_ALL_PERSONALRESSURS',
  status: 'UPSTREAM_QUEUE',
  time: new Date().getTime(),
  orgId: '',
  source: 'fk',
  client: 'vfs',
  message: null,
  data: [
    {
      resource: {
        ansattnummer: {
          identifikatorverdi: '10025'
        }
      },
      relasjoner: [
        {
          relationName: 'person',
          link: '{no.fint.model.felles.Person}/person/fodselsnummer/204194497763'
        }
      ]
    }
  ]
}
