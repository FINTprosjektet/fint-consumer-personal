module.exports = {
  corrId: '',
  action: 'GET_ALL_PERSON',
  status: 'UPSTREAM_QUEUE',
  time: new Date().getTime(),
  orgId: '',
  source: 'fk',
  client: 'vfs',
  message: null,
  data:
  [
    {
      resource: {
        fodselsnummer: {
          identifikatorverdi: '204194497763'
        }
      },
      relasjoner: [
        {
          relationName: 'personalressurs',
          link: '{no.fint.model.administrasjon.personal.Arbeidsforhold}/personalressurs/ansattnummer/10025'
        }
      ]
    }
  ]
}
