package fr.evref.modest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jacoco.agent.rt.RT;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

/**
 * Execute JUnit tests and export the coverage of each atomic test method. Not
 * thread-safe.
 */
public class AtomicCoverageAnalyzer extends RunListener {

	private static final PrintStream stdout = System.out;

	/**
	 * Expect a directory path to output coverage data, and fully qualified JUnit
	 * case/suite names.
	 */
	public static void main(String[] args) {
		// ensure arguments
		if (args.length < 2) {
			error("Expected a directory path to output coverage data, and at least one fully qualified JUnit case/suite name.");
			return;
		}

		// ensure output directory
		String outputDirectory;
		try {
			outputDirectory = Files.createDirectories(Paths.get(args[0])).toString();
		} catch (IOException e) {
			error(e);
			return;
		}

		// ensure requested classes
		Class<?>[] classes = new Class[args.length - 1];
		try {
			for (int i = 0; i < classes.length; i++) {
				classes[i] = Class.forName(args[i + 1]);
			}
		} catch (ClassNotFoundException e) {
			error(e);
			return;
		}

		// suppress stdout
		System.setOut(new PrintStream(new OutputStream() {
			public void write(int b) {
				// DO NOTHING
			}
		}));

		// run analysis
		new AtomicCoverageAnalyzer(outputDirectory).run(classes);

		// announce success on stdout
		stdout.println("DONE");

		// ensure terminate
		System.exit(0);
	}

	private static void error(String message) {
		System.out.println("ERROR");
		System.err.println(message);
	}

	private static void error(Exception e) {
		System.out.println("ERROR");
		e.printStackTrace();
	}

	private String outputDirectory;

	public AtomicCoverageAnalyzer() {
	}

	public AtomicCoverageAnalyzer(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	/** Obtain coverage data of each test method in the given test classes. */
	public Result run(Class<?>... testClasses) {
		JUnitCore engine = new JUnitCore();
		engine.addListener(this);
		return engine.run(testClasses);
	}

	/**
	 * Reset JaCoCo before each test method. This is necessary instead of resetting
	 * with {@link org.jacoco.agent.rt.IAgent#getExecutionData(boolean)
	 * getExecutionData(boolean reset)} to avoid polluting the very first report.
	 */
	@Override
	public void testStarted(Description description) {
		RT.getAgent().reset();
	}

	/**
	 * Output the coverage report of an atomic test method in the
	 * <code>outputFolder</code>.
	 */
	@Override
	public void testFinished(Description description) throws IOException {
		// set a specific destination file per test method
		String filename = description.getClassName() + "." + description.getMethodName() + ".exec";
		String filepath = Paths.get(outputDirectory, filename).toString();

		// output data to file
		try (FileOutputStream out = new FileOutputStream(filepath)) {
			byte[] data = RT.getAgent().getExecutionData(false); // no reset
			out.write(data);
		}
	}

}
