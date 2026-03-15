package at.mateball.domain.group.core.assembler;

import at.mateball.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MatchImageAssembler {

    private final FileStorage fileStorage;

    public Map<Long, List<String>> assemble(List<? extends MatchImageSource> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyMap();
        }

        return images.stream()
                .collect(Collectors.groupingBy(
                        MatchImageSource::matchId,
                        LinkedHashMap::new,
                        Collectors.mapping(
                                image -> fileStorage.getImageUrl(image.profileImageKey()),
                                Collectors.toList()
                        )
                ));
    }

    public List<String> getImages(Map<Long, List<String>> imageMap, Long matchId) {
        return imageMap.getOrDefault(matchId, Collections.emptyList());
    }

    public interface MatchImageSource {
        Long matchId();
        String profileImageKey();
    }
}
