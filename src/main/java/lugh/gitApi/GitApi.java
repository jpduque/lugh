package lugh.gitApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;


public class GitApi {
    public void getGitRepo(String gitUrl, String branch) throws IOException{
        gitUrl = "https://github.com/" + gitUrl + "/archive/"+ branch +".zip";
        System.out.println(gitUrl);
        String temporalFolder = System.getProperty("user.dir")+"/temp";
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

    private Connection.Response getAllCommits(String project) throws IOException {
        String url = "https://api.github.com/repos/"+ project +"/commits";
         return Jsoup.connect(url)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .followRedirects(true)
                .timeout(10000)
                .method(Connection.Method.GET)
                .maxBodySize(0)
                .execute();
    }

    public void cleanTemporal() throws IOException {
        String temporalFolder = System.getProperty("user.dir")+"/temp/src";
        Path directory = Paths.get(temporalFolder);
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                Files.delete(file); // this will work because it's always a File
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir); //this will work because Files in the directory are already deleted
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public List<String> commitList(String project)throws IOException {
        List<String> commitList = new ArrayList<>();
        Connection.Response response = getAllCommits(project);
        ObjectMapper mapper = new ObjectMapper();
        Response[] gitCommits = mapper.readValue(response.body(), Response[].class);
        for(Response res : gitCommits){
            commitList.add(res.getCommit().getTree().getSha());
        }
        return commitList;
    }
}