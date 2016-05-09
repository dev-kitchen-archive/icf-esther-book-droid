package kitchen.dev.icfbooks.model.media;

/**
 * Created by samue on 5/9/2016.
 */
public class MediaTypes {
    public class MovieMedia extends Media<Movie> {}
    public class TwoMoviesAndTextMedia extends Media<TwoMoviesAndText> {}

    public class Movie {
        private String file_url;

        public String getFile_url() {
            return file_url;
        }

        public void setFile_url(String file_url) {
            this.file_url = file_url;
        }
    }

    public class TwoMoviesAndText {

    }
}
