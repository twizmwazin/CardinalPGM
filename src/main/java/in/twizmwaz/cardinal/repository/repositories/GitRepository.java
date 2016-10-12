package in.twizmwaz.cardinal.repository.repositories;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.repository.exception.RotationLoadException;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.TransportCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class GitRepository extends Repository {

    private Git git;
    private CredentialsProvider credentials = null;
    private URIish gitUrl;

    @Override
    public String getSource(boolean op) {
        return op || credentials == null ? format(gitUrl) : null;
    }

    private GitRepository(URIish uri) throws RotationLoadException, IOException {
        super(Cardinal.getNewRepoPath(DigestUtils.md5Hex(format(uri))));
        this.gitUrl = uri;
        if (uri.getUser() != null && uri.getPass() != null)
            this.credentials = new UsernamePasswordCredentialsProvider(uri.getUser(), uri.getPass());
    }

    public static GitRepository fromURL(String url) throws RotationLoadException, IOException {
        URIish uri;
        try {
            uri = new URIish(url);
        } catch (URISyntaxException e) {
            throw new RotationLoadException("Invalid URI for repository:" + url);
        }
        return new GitRepository(uri);
    }

    @Override
    public void refreshRepo() throws RotationLoadException, IOException {
        try {
            if (git == null) {
                if (new File(getPath() + File.separator + ".git").exists())
                    this.git = Git.open(new File(getPath()));
                else
                    this.git = ((CloneCommand) addCredentials(
                            Git.cloneRepository().setURI(gitUrl.toString()).setDirectory(new File(getPath())))).call();
            }
            git.clean().call();
            addCredentials(git.fetch()).call();
            git.reset().setRef("@{upstream}").setMode(ResetCommand.ResetType.HARD).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new RotationLoadException("Could not load git repository: " + gitUrl);
        }
        super.refreshRepo();
    }

    private TransportCommand addCredentials(TransportCommand command) {
        if (credentials != null) command.setCredentialsProvider(credentials);
        return command;
    }

    private static String format(URIish uri) {
        StringBuilder r = new StringBuilder();

        if (uri.getScheme() != null) r.append(uri.getScheme()).append("://");

        if (uri.getHost() != null) {
            r.append(uri.getHost());
            if (uri.getScheme() != null && uri.getPort() > 0) r.append(':').append(uri.getPort());
        }

        if (uri.getPath() != null) {
            if (uri.getScheme() != null) {
                if (!uri.getPath().startsWith("/") && !uri.getPath().isEmpty()) r.append('/');
            } else if(uri.getHost() != null) r.append(':');

            if (uri.getScheme() != null) r.append(uri.getRawPath());
            else r.append(uri.getPath());
        }

        return r.toString();
    }

}
