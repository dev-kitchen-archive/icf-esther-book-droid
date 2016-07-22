package kitchen.dev.icfbooks.esther.model.books;

import java.util.ArrayList;
import java.util.List;

import kitchen.dev.icfbooks.esther.model.media.Media;

/**
 * Created by samue on 03/21/16.
 */
public class BookFactory {

    public List<Book> getAllBooks(){

        //return Ester book hardcoded
        List<Book> books = new ArrayList<>();
        books.add(new Book(){
            @Override
            public String getTitle() {
                return "Esther";
            }

            @Override
            public String getAuthor() {
                return "Leo Bigger";
            }

            @Override
            public String getDescription() {
                return "Esther in da House";
            }

            @Override
            public String getImage_url() {
                return "http://thesource.icf-download.ch/icf_zurich/01_GenX/2015/02_Esther/04_Multimedia/01_Design/JPEGS/esther-seriendesign.jpg";
            }

            @Override
            public int getId() {
                return 1;
            }
        });

        return books;
    }
}
