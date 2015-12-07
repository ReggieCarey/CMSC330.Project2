Window "Calculator" (300, 325) Layout Flow:
  Textfield 20;
  Panel Layout Grid(3,1):
    Panel Layout Flow:
      Group
        Radio "Programmer";
        Radio "Basic";
        Radio "Scientific";
      End;
    End;
    Panel Layout Flow:
      Group
        Radio "Basic";
        Radio "Scientific";
        Radio "Programmer";
      End;
    End;
    Label "CMSC 330 - Reggie Carey";
  End;
  Panel Layout Grid(5, 4, 5, 5):
    Button "AC";
    Button "+/-";
    Button "%";
    Button "/";
    
    Button "7";
    Button "8";
    Button "9";
    Button "*";
    
    Button "4";
    Button "5";
    Button "6";
    Button "-";
    
    Button "1";
    Button "2";
    Button "3";
    Button "+";
    
    Button "0";
    Button "<-";
    Button ".";
    Button "=";
  End;
End.
