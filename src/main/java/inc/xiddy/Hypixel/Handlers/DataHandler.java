package inc.xiddy.Hypixel.Handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import inc.xiddy.Hypixel.Main;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class DataHandler {
	private final ObjectMapper mapper;
	private final String basepath;

	public DataHandler(String basePath) {
		// Create JSON object mapper
		this.mapper = new ObjectMapper();
		// Strip unneeded slashes and set to field
		this.basepath = this.stripSlashes(basePath);
	}

	public <T> void write(String filename, T obj) {
		// Try in case of file error
		try {
			// Write object to JSON file
			this.mapper.writeValue(this.filenameToFile(filename, true), obj);
		} catch (IOException e) {
			// If found, then find reason and catch
			e.printStackTrace();
		}
	}

	public <T> T read(String filename, Class<T> cls) throws FileNotFoundException {
		// Try in case of file error
		try {
			// Convert to object and return
			return this.mapper.readValue(this.filenameToFile(filename, false), cls);
		} catch (MismatchedInputException e) {
			// Better alias and explanation for the error
			Main.getMainHandler().getLogger().error("Invalid/corrupt JSON read [MIGHT REQUIRE .m2\\repository DELETION, SEE https://stackoverflow.com/questions/32090921/deploying-maven-project-throws-java-util-zip-zipexception-invalid-loc-header-b] (MismatchedInputException): ");
//			e.printStackTrace();
			throw new FileNotFoundException();
		} catch (IOException e) {
			// Better alias and explanation for the error
			throw new FileNotFoundException();
		}
	}

	private File filenameToFile(String filename, boolean makeDirs) {
		// Strip the slashes on the filename, get rest of path to file
		String strippedPath = this.stripSlashes(filename);
		// If we should create the filepath
		if (makeDirs) {
			// Get path to file
			File semiPath = new File(this.getBasepath() + "\\" + strippedPath.substring(0, strippedPath.lastIndexOf("\\")));
			// Make the directory if it doesn't exist
			//noinspection ResultOfMethodCallIgnored
			semiPath.mkdirs();
		}
		// Return the full path as a File object
		return new File(this.getBasepath() + "\\" + strippedPath);
	}

	public String stripSlashes(String path) {
		// Similar to .strip('\\') in Python
		return path.replaceAll("^[\\\\]+|[\\\\]+$", "");
	}

	public String getBasepath() {
		return basepath;
	}

	public void removeFolder(String sourceFolder) throws IOException {
		this.removeFolder(new File(sourceFolder));
	}

	public void removeFolder(File sourceFolder) throws IOException {
		FileUtils.deleteDirectory(sourceFolder);
	}

	public void copyFolderContents(File sourceFolder, String destFolder, String[] ignore) {
		this.copyFolderContents(sourceFolder, sourceFolder.getPath(), destFolder, ignore);
	}

	private void copyFolderContents(File sourceFolder, String originalFolder, String destFolder, String[] ignore) {
		// Make sure file is not on the 'ignore' list
		for (String filename : ignore) {
			if (filename.equals(sourceFolder.getName())) {
				return;
			}
		}
		// Create path if it doesn't exist
		File destPath = new File(this.stripSlashes(destFolder + sourceFolder.getPath().substring(originalFolder.length())));
		//noinspection ResultOfMethodCallIgnored
		destPath.mkdirs();
		// If path is a directory
		if (sourceFolder.isDirectory()) {
			// For each item in the directory
			for (File file : Objects.requireNonNull(sourceFolder.listFiles())) {
				// Recurse to copy folders and files
				this.copyFolderContents(file, originalFolder, destFolder, ignore);
			}
		} else {
			// If the path is a file, try to...
			try {
				// Copy the file to the new folder
				Files.copy(sourceFolder.toPath(), destPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// If found, then find reason and catch
				e.printStackTrace();
			}
		}
	}
}
