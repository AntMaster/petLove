package com.shumahe.pethome.Util.LambdaUtil;

import com.shumahe.pethome.Domain.PetPublish;
import com.shumahe.pethome.Util.LambdaUtil.Domain.Album;
import com.shumahe.pethome.Util.LambdaUtil.Domain.Artist;
import com.shumahe.pethome.Util.LambdaUtil.Domain.Track;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class StreamPractise {


    void collectStream() {

        List<String> collected = Stream.of("a", "b", "c").collect(Collectors.toList());
        List<String> collected2 = Stream.of("a", "b", "c").collect(toList());
        //assertEquals(Arrays.asList("a", "b", "c"), collected);
    }

    void mapStream() {

        // map接受一个Function接口的实例 ?
        // Function<? super T, ? extends R> mapper

        //String ---> domain
        Function<String, Map> function = (String s) -> {
            Map<String, PetPublish> map = new HashMap<>();
            map.put(s, new PetPublish());
            return map;
        };
        List<Map> collect = Stream.of("a", "b", "hello").map(function).collect(toList());

        //domain ---> String


        //Stream.of("a", "b", "hello").map()

    }


    void filterStream() {

        // map接受一个或者多个Predicate接口
        Predicate<String> pre = s -> s.contains("123");
        Stream.of("a", "b", "hello").filter(pre);

        Stream.of("a", "b", "hello").filter(s -> s.contains("123"));

    }


    static void flatMapStream() {

        //flatMap 方法可用Stream 替换值， 然后将多个Stream 连接成一个Stream
        List<String> together = Stream.of(Arrays.asList("1", "2"), Arrays.asList("3"))
                .flatMap(numbers -> numbers.stream())
                .collect(toList());

        //将多个流串联起来
        List<PetPublish> collect = Stream.of(Arrays.asList("1", "2"), Arrays.asList(3, 3, 4)).flatMap(e -> e.stream()).distinct().map(e -> {
            PetPublish pet = new PetPublish();
            pet.setPetName(String.valueOf(e));
            return pet;
        }).collect(toList());
        System.out.println(collect);

    }


    static void maxAndMinStream() {

        List<Track> tracks = Arrays.asList(new Track("Bakai", 524), new Track("Violets for Your Furs", 524), new Track("Time Was", 451));

        Optional<Track> max = tracks.stream().max(Comparator.comparing(track -> track.getLength()));
        Track track1 = max.get();


        tracks.sort(Comparator.comparing(track -> track.getLength()));

    }


    static void reduceStream() {

        //reduce 操作可以实现从一组值中生成一个值。在上述例子中用到的count、min 和max 方法，因为常用而被纳入标准库中。事实上，这些方法都是reduce 操作。

        //0 初始值//acc 累加器 //element 当前元素
        BinaryOperator<Integer> binaryOperator = (acc, element) -> acc + element;
        Stream.of(1, 2, 3, 4).reduce(0, binaryOperator);

        //直接写
        Stream.of(1, 2, 3, 4).reduce(0, (acc, element) -> acc + element);


        //展开reduce操作
        BinaryOperator<Integer> accumulator = (acc, element) -> acc + element;
        int count = accumulator.apply(accumulator.apply(accumulator.apply(0, 1), 2), 3);

    }

    public static void main(String[] args) {

        //maxAndMinStream();


    }

    int addUp(Stream<Integer> number) {
        return number.reduce(0, (acc, x) -> acc + x);
    }

    List<String> getNameAndNation(List<Artist> artists) {

        Map<Boolean, List<Artist>> collect = artists.stream().collect(partitioningBy(artist -> artist.getName().isEmpty()));
        String collect1 = artists.stream().map(Artist::getName).collect(joining(",", "[", "]"));

        artists.stream().collect(groupingBy(artist -> artist.getName(), counting()));

        return null;

    }

  /*  StringCombiner combined =
            artists.stream()
                    .map(Artist::getName)
                    .reduce(new StringCombiner(", ", "[", "]"),
                            StringCombiner::add,
                            StringCombiner::merge);
    String result = combined.toString();*/

    public Map<Artist, List<String>> nameOfAlbumsDumb(Stream<Album> albums) {
        Map<Artist, List<Album>> albumsByArtist = albums.collect(groupingBy(album -> album.getMainMusician()));
        Map<Artist, List<String>> nameOfAlbums = new HashMap<>();
        for (Map.Entry<Artist, List<Album>> entry : albumsByArtist.entrySet()) {
            nameOfAlbums.put(entry.getKey(), entry.getValue()
                    .stream()
                    .map(Album::getName)
                    .collect(toList()));
        }
        return nameOfAlbums;
    }


    public Map<Artist, List<String>> nameOfAlbums(Stream<Album> albums) {


        Map<Artist, List<PetPublish>> collect = albums.collect(groupingBy(Album::getMainMusician, mapping(e -> {
            PetPublish pet = new PetPublish();
            return pet;
        }, toList())));

        return albums.collect(groupingBy(Album::getMainMusician, mapping(Album::getName, toList())));


    }



    List<String> getAlbumList(List<String> artists) {

        //List<Artist> artists = Arrays.asList(new Artist(), new Artist(), new Artist());
     /*   int totalMembers = 0;
        for (Artist artist : artists) {
            Stream<Artist> members = artist.getMembers();
            totalMembers += members.count();
        }
*/
        //artists.stream().reduce(0, (x, y) -> x + y);


        return null;
    }
}
