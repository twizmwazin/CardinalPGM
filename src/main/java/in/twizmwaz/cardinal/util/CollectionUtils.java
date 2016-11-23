package in.twizmwaz.cardinal.util;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class CollectionUtils {

    /**
     * Updates an old collection, to be the new collection, but keeping the old objects if they equal to true.
     * @param oldCol Old collection,
     * @param newCol The result you want to get.
     * @return Stream newCol items, but replacing them with the ones that are equal to the old ones.
     */
    public static <T> Stream<T> update(Collection<T> oldCol, Collection<T> newCol) {
        return Stream.concat(
                oldCol.stream().filter(newCol::contains),                //Remove no longer present objects from old col
                newCol.stream().filter(newIt -> !oldCol.contains(newIt)));// Remove already present objects from new col
    }

    /**
     * Updates an old collection, to be the new collection, but keeping the old objects if they equal to true.
     * @param oldCol Old collection,
     * @param newCol The result you want to get.
     * @return Stream newCol items, but replacing them with the ones that are equal to the old ones.
     */
    public static <T> Stream<T> updateAndRun(List<T> oldCol, List<T> newCol, BiConsumer<T, T> run) {
        return Stream.concat(
                oldCol.stream().filter(newCol::contains),                //Remove no longer present objects from old col
                                                  // Remove already present objects from new col, but updating old items
                newCol.stream().filter(it -> !containsAndUpdate(it, oldCol, run)));//
    }

    private static <T> boolean containsAndUpdate(T item, Collection<T> oldCol, BiConsumer<T, T> run) {
        Optional<T> oldItem = oldCol.stream().filter(oldIt -> oldIt.equals(item)).findFirst();
        oldItem.ifPresent(oldIt -> run.accept(oldIt, item));
        return oldItem.isPresent();
    }

}
