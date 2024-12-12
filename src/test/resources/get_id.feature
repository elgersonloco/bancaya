Feature: Obtener el id de un pokemon
  Scenario: Se desea obtener el id de un pokemon mediante su nombre
    Given Obtener el id de un pokemon llamado "pikachu"
    When Se procesa la solitud de obtener el id del pokemon
    Then Se debe retornar el id del pokemon