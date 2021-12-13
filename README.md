convert binary to a function

- likely a wave function would work best
- could be recursive
- probably could use shortcuts to decrease size 
  - shortcut idea: instead of storing the function, have one universal function and just store the distance between the numbers in the function. example would be: function outputs "01000101", we are looking for pattern "000" so we skip 2 numbers to find our sequence. 
  - shortcut idea: instead of storing distance from eachother each bit of the sequence is, find an occurance of the sequence we are trying to compress in the function and store its distance from 0 instead. potential downside: this could not actually save space depending on how far the number is from 0. my reasoning: binary goes between 0 and 1, and functions can go between 0 and 1. In this case, we could convert the binary to a binary representation of the same binary but as a function, which could take up less space at the cost of doing some math. i would imagine that wave functions would likely work best in this scenario, as they naturalle oscillate between 1 and -1, and with some transformation, they would likely take up less space to do so compared to other functions.
  - shortcut idea: the binary waveform for sequence "1111" may be the same for sequence "111100", so when storing the second sequence i could maybe just store the first sequence