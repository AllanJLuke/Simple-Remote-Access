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
     * modified from
     * src:http://www.mkyong.com/java/how-to-execute-shell-command-from-java/
     * @param command
     * @return
     */
    public String executeCommand(final String command) {
        
        String filteredCommand = filterCommand(command);

        if (command != null) {
            ArrayList<String> outputLines = new ArrayList<String>();
            ArrayList<String> errorLines = new ArrayList<String>();


            Process p;
            try {
                if (isLinux)
                    p = Runtime.getRuntime().exec(new String[]{"/bin/sh",filteredCommand});
                else
                    p = Runtime.getRuntime().exec(new String[]{"cmd.exe","/c", filteredCommand});
                p.waitFor();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader errorReader =
                        new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    outputLines.add(line + "\n");
                }

                while ((line = errorReader.readLine()) != null) {
                    errorLines.add(line + "\n");
                }


                StringBuilder builder = new StringBuilder();
                if (errorLines.size() == 0)
                {
                    if (command.startsWith("cd"))
                    {
                        workingDirectory = command.split(" ")[1];
                    }


                    outputLines.forEach(builder::append);
                    return builder.toString();
                }
                else
                {

                    errorLines.forEach(builder::append);
                    return builder.toString();
                }




            } catch (Exception e) {
                e.printStackTrace();
                return  null;
            }

        }
        else return "INVALID COMMAND";
    }

    private String setWorkingDir() {
        if (workingDirectory != null)
             return ("cd "+ workingDirectory) + (isLinux ? ";":" && ");
        return "";
    }



    private String filterCommand(String command) {
        if (isValidCommand(command))
        {

            if (command.startsWith("ls"))
            {
             command = command.replace("ls","dir");
            }
            return setWorkingDir() + command;
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
