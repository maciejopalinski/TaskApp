run:
	javac -d out/ `find src/ -name "*.java"`
	java -cp out/ io.github.poprostumieciek.taskapp.TaskAppGUI

format:
	astyle --project=.astylerc `find src/ -name "*.java"`