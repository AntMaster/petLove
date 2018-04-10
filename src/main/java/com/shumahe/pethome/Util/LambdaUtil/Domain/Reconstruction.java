package com.shumahe.pethome.Util.LambdaUtil.Domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Reconstruction {
    /**
     * 如何将一段使用循环进行集合操作的代码，重构成基于Stream 的操作。
     * <p>
     * 假定选定一组专辑，找出其中所有长度大于1 分钟的曲目名称。findLongTracks_remain 是遗留代码，
     * 首先初始化一个Set 对象，用来保存找到的曲目名称。然后使用for 循环遍历所有专辑，每次
     * 循环中再使用一个for 循环遍历每张专辑上的每首曲目，检查其长度是否大于60 秒，如
     * 果是，则将该曲目名称加入Set 对象。
     */

    public Set<String> findLongTracks_remain(List<Album> albums) {
        Set<String> trackNames = new HashSet<>();
        for (Album album : albums) {
            for (Track track : album.getTrackList()) {
                if (track.getLength() > 60) {
                    String name = track.getName();
                    trackNames.add(name);
                }
            }
        }
        return trackNames;
    }


    /**
     * 最内层的forEach 方法有三个功用：
     * 找出长度大于1 分钟的曲目，
     * 得到符合条件的曲目名称，
     * 将曲目名称加入集合Set
     * <p>
     * <p>
     * <p>
     * 这就意味着需要三项Stream 操作: 找出满足某种条件的曲目是filter 的功能，得到曲目名称则可用map 达成，终结操作可使用forEach 方法将曲目流名称加入一个集合。
     *
     * @param albums
     * @return
     */
    public Set<String> findLongTracks_Step1(List<Album> albums) {
        Set<String> trackNames = new HashSet<>();
        albums.stream()
                .forEach(album -> album.getTracks().
                        forEach(track -> {
                            if (track.getLength() > 60) {
                                String name = track.getName();
                                trackNames.add(name);
                            }

                        }));
        return trackNames;
    }


    /**
     * 操作：用以上三项Stream 操作将内部的forEach 方法拆分后
     * 现在用更符合流风格的操作替换了内层的循环，但代码看起来还是冗长繁琐。将各种流嵌套起来并不理想，最好还是用干净整洁的顺序调用一些方法。
     *
     * @param albums
     * @return
     */
    public Set<String> findLongTracks_Step2(List<Album> albums) {
        Set<String> trackNames = new HashSet<>();

        albums.stream()
                .forEach(album -> album.getTracks()
                        .filter(track -> track.getLength() > 60)
                        .map(track -> track.getName())
                        .forEach(name -> trackNames.add(name)));
        return trackNames;
    }


    /**
     * @param albums
     * @return
     */
    public Set<String> findLongTracks_Step3(List<Album> albums) {
        Set<String> trackNames = new HashSet<>();
        albums.stream()
                .flatMap(album -> album.getTracks())
                .filter(track -> track.getLength() > 60)
                .map(track -> track.getName())
                .forEach(name -> trackNames.add(name));
        return trackNames;
    }


    public Set<String> findLongTracks_final(List<Album> albums) {

        return albums.stream()
                .flatMap(album -> album.getTracks())
                .filter(track -> track.getLength() > 60)
                .map(track -> track.getName())
                .collect(Collectors.toSet());
    }

}
