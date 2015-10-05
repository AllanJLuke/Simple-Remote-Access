package utils;

import server.SocketServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by allan on 2015-09-28.
 *
 */
public class ShellEmu {
    private String workingDirectory = null;

    private final boolean isLinux;

    public ShellEmu(boolean isLinux)
    {
        this.isLinux = isLinux;
        workingDirectory = executeCommand(isLinux ? "pwd" : "echo %cd%").replaceAll("\\r?\\n", "");
    }





    public String executeCommand(final String command) {
        
        String filteredCommand = filterCommand(command);

        if (filteredCommand != null) {
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
                    String[] split = command.split(" ");
                    if (split[0].equals("cd"))
                    {
                        if (!split[1].equals("..")) {
                            File temp = new File(split[1]);

                            if (temp.isAbsolute())
                                workingDirectory = split[1].replaceAll("\\r?\\n", "");
                            else{
                                workingDirectory+=File.separator+split[1];
                            }
                        }
                        else
                        {

                           workingDirectory =  workingDirectory.substring(0,workingDirectory.lastIndexOf(File.separator));
                        }
                    }

                    if (outputLines.size() == 0)
                        return "Current Directory: " + workingDirectory;
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
        else return SocketServer.showHelp();
    }

    private String setWorkingDir() {
        if (workingDirectory.length() > 0)
             return ("cd "+ workingDirectory) + (isLinux ? ";":" && ");
        return workingDirectory;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
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
        else if (workingDirectory == null)
        {
            return command;
        }
        return null;
    }

    private boolean isValidCommand(String command) {
        String[] commandSplit = command.split(" ");

       switch(commandSplit[0].trim()){
           case "ls":
           case "cd":
           case "mkdir":
               return true;
           default:
               return false;
       }

    }

}
