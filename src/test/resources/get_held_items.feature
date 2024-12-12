Feature: Obtener los items de pokemon
  Scenario: Se desea obtener los items de un pokemon mediante su nombre
    Given Obtener los items de un pokemon llamado "pikachu"
    When Se procesa la solitud de obtener los items del pokemon
    Then Se debe retornar los items del pokemon