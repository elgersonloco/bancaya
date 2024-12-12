Feature: Obtener los lugares donde se encuentra el pokemon
  Scenario: Se desea obtener los lugares donde se encuentra pokemon mediante su nombre
    Given Obtener los lugares donde se encuentra un pokemon llamado "pikachu"
    When Se procesa la solitud de obtener los lugares donde se encuentra el pokemon
    Then Se debe retornar los lugares donde se encuentra el pokemon