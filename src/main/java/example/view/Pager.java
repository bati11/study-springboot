package example.view;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Pager implements Serializable {

    private int lastPage;
    private int currentPage;
    private int startPage;
    private int endPage;
    private boolean hasPrevious;
    private boolean hasNext;

    private List<Integer> pages = new ArrayList<>();

    public Pager(int currentPage, int totalCount, int countPerPage) {
        this.currentPage = currentPage;
        this.lastPage = (int)Math.ceil((double)totalCount / countPerPage);
        this.startPage = (currentPage - 2) < 1 ? 1 : (currentPage - 2);
        this.endPage = (currentPage + 2) < lastPage ? (currentPage + 2) : lastPage;
        this.hasPrevious = currentPage > 1;
        this.hasNext = currentPage < lastPage;

        for (int i = startPage; i <= endPage; i++) {
            pages.add(i);
        }
    }
}
