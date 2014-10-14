ImgToID3
========
ImgToID3 is a simple program for batch tagging mp3 files with images found in the same folder or folders relative to them.

Running
-------
Edit the config.cfg to suit your needs:
  - path to the folder to the music files (it will scan it and all subfolders)
  - do you want to overwrite files already tagged with albumart?
  - were do you want the program to look for images, relative to the mp3's?
Then as a windows user just run RunOnWindows.bat
for other os's, run ImgToID3.jar in a terminal.

when run it will first assess files and open a textfile with information about pending writes, letting you accept or exit.
after writing tags it will open another textfile informing you of changes made.


it makes use of the mp3agic library found at https://github.com/mpatric/mp3agic