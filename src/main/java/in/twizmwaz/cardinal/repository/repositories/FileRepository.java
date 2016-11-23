package in.twizmwaz.cardinal.repository.repositories;

import in.twizmwaz.cardinal.repository.exception.RotationLoadException;

import java.io.IOException;

public class FileRepository extends Repository {

    public FileRepository(String path) throws RotationLoadException, IOException {
        super(path);
    }

}
