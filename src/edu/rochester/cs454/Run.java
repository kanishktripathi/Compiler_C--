package edu.rochester.cs454;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Run {

	public static void main(String args[]) {
		String fileName = null;
		if (args.length == 0) {
			System.out.println("No file specified");
		} else {
			fileName = args[0];
			try {
				Scanner scan = new Scanner();
				String fileContent = new String(Files.readAllBytes(Paths.get(fileName)));
				List<Token> tokens = scan.getTokens(fileContent);
				LLParser parser = new LLParser(tokens);
				String output = parser.parseLL();
				fileName = fileName + "_out.c";
				Files.write(Paths.get(fileName), output.getBytes("utf-8"), StandardOpenOption.CREATE, 
						StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
				System.out.println("Error in File input:" + e);
			} catch (ParsingException | NumberFormatException e) {
				System.out.println(e.getMessage() + e);
				System.out.println("Fail");
			}
		}
	}
}
