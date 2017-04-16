module.exports = {
  corrId: '',
  action: 'GET_ALL_ARBEIDSFORHOLD',
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
        systemId: {
          identifikatorverdi: '6db6fcd3-3edc-43b4-b0d4-07adb71d486e'
        },
        aktiv: false,
        arslonn: 0.0,
        ansettelsesprosent: 0.0,
        lonnsprosent: 0.0,
        hovedstilling: false
      },
      relasjoner: [
        {
          relationName: 'personalressurs',
          link: '${no.fint.model.administrasjon.personal.Personalressurs}/ansattnummer/10025'
        }
      ]
    }
  ]
}
