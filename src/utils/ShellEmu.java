package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by allan on 2015-09-28.
 */
public class ShellEmu {
    private String workingDirectory = null;

    private final boolean isLinux;

    public ShellEmu(boolean isLinux)
    {
        this.isLinux = isLinux;
    }

    /**
     * src:http://www.mkyong.com/java/how-to-execute-shell-command-from-java/
     * @param command
     * @return
     */
    public String executeCommand(String command) {
        
        command = filterCommand(command);

        if (command != null) {
            ArrayList<String> outputLines = new ArrayList<String>();
            ArrayList<String> errorLines = new ArrayList<String>();


            Process p;
            try {
                if (isLinux)
                    p = Runtime.getRuntime().exec(new String[]{"/bin/sh",setWorkingDir() + command + addWorkingDir()});
                else
                    p = Runtime.getRuntime().exec(new String[]{"cmd.exe","/c",setWorkingDir() + command + addWorkingDir()});
                p.waitFor();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader errorReader =
                        new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line = "";
                reader.lines();
                while ((line = reader.readLine()) != null) {
                    outputLines.add(line + "\n");
                }

                while ((line = errorReader.readLine()) != null) {
                    errorLines.add(line + "\n");
                }

//               workingDirectory = outputLines.get(outputLines.size()-1);


//                if (output.length() > 0)
//                {
//                    if (command.)
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            StringBuilder builder = new StringBuilder();

            outputLines.forEach(builder::append);
            return builder.toString();
        }
        else return "INVALID COMMAND";

    }

    private String setWorkingDir() {
        if (workingDirectory != null)
             return ("cd "+ workingDirectory) + (isLinux ? ";":"&&");
        return "";
    }

    private String addWorkingDir() {
        return isLinux ? ";pwd;" : "&&echo %cd%";
    }



    private String filterCommand(String command) {
        if (isValidCommand(command))
        {
            if (command.startsWith("ls"))
            {
             return command.replace("ls","dir");
            }
            return command;
        }
        return null;
    }

    private boolean isValidCommand(String command) {
        String[] commandSplit = command.split(" ");

       switch(commandSplit[0].trim()){
           case "ls":
           case "get":
           case "put":
           case "cd":
           case "mkdir":
               return true;
           default:
               return false;
       }

    }

}
