
int cnt = 0;
#define N 10

active [2] proctype Thread() {
  int i; 
  do
  :: (i < N) ->
     int reg = cnt;
     cnt = reg + 1;
     i = i + 1;
     
  :: else -> 
     break;
  od 
}

// Acceptance
ltl minCntValue {
  <>([](cnt >= 3))
}
