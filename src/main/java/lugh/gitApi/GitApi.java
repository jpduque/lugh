package lugh.gitApi;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;


public class GitApi {
    public void getGitRepo(String gitUrl, String branch) throws IOException{
        gitUrl = gitUrl + "/archive/"+ branch +".zip";
        String temporalFolder = this.getClass().getClassLoader().getResource("temp").getFile();
        Connection.Response response = Jsoup.connect(gitUrl)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .followRedirects(true)
                .timeout(10000)
                .method(Connection.Method.GET)
                .maxBodySize(0)
                .execute();

        saveSources(response, temporalFolder);
        unzipSrc(temporalFolder);
    }

    private void saveSources(Connection.Response response, String temporalFolder) {
        BufferedInputStream inputStream = response.bodyStream();
        try {
            FileOutputStream fos = new FileOutputStream(temporalFolder + "/src.zip");
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            inputStream.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void unzipSrc(String temporalFolder){
        try {
            ZipFile zipFile = new ZipFile(temporalFolder + "/src.zip");
            zipFile.extractAll(temporalFolder + "/src");
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
}