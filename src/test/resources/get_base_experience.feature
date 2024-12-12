Feature: Obtener la experiencia base de un pokemon
  Scenario: Se desea obtener la experiencia base de un pokemon mediante su nombre
    Given Obtener la experiencia base de un pokemon llamado "pikachu"
    When Se procesa la solitud de obtener la experiencia base del pokemon
    Then Se debe retornar la experiencia base del pokemon