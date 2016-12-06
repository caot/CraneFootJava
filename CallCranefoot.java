import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import ConstantString;

public class CallCranefoot {
  protected boolean callCranefoot;

  public boolean isCallCranefoot() {
    return callCranefoot;
  }

  public String getFileName(String szFamilyID, String svRandomNum) {
    return this.getFileName(szFamilyID, svRandomNum, ".txt");
  }

  private String getFileName(String szFamilyID, String svRandomNum, String ext) {
    String fileName = "pedigree" + szFamilyID + "_" + svRandomNum + ext;
    return fileName;
  }

  public String getLogFileName(String szFamilyID, String svRandomNum) {
    return this.getFileName(szFamilyID, svRandomNum, ".log");
  }

  public String getTopologyFileName(String szFamilyID, String svRandomNum) {
    return this.getFileName(szFamilyID, svRandomNum, ".topology.txt");
  }

  public String getPSFileName(String szFamilyID, String svRandomNum) {
    return this.getFileName(szFamilyID, svRandomNum, ".ps");
  }

  public String getPDFFileName(String szFamilyID, String svRandomNum) {
    return this.getFileName(szFamilyID, svRandomNum, ".pdf");
  }

  protected boolean callCranefoot(String szFamilyID, String svRandomNum) {

    String dir = ConstantString.getInstance().exportDir();
    File dirf = new File(dir);
    Runtime rt = Runtime.getRuntime();
    String[] cmds = {
        "cranefoot config.txt " + this.getFileName(szFamilyID, svRandomNum),
        "ps2pdf " + this.getPSFileName(szFamilyID, svRandomNum) + " " + this.getPDFFileName(szFamilyID, svRandomNum),
    };

    FileWriter filew = null;
    try {
      filew = new FileWriter(dir + this.getLogFileName(szFamilyID, svRandomNum));
      final BufferedWriter out = new BufferedWriter(new FileWriter(dir + this.getLogFileName(szFamilyID, svRandomNum)));

      for (String cmd : cmds) {
        System.out.println(cmd);
        Thread.sleep(500);
        final Process p = rt.exec(cmd, null, dirf);

        new Thread(new Runnable() {
          public void run() {
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;

            try {
              while ((line = input.readLine()) != null) {
                System.out.println(line);
                out.write(line);
                out.write("\n");
              }
            } catch (IOException e) {
              e.printStackTrace();
            } finally {
              try {
                input.close();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }
        }).start();
        p.waitFor();
      }
      out.close();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      try {
        if (filew != null)
          filew.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return false;
  }
}
