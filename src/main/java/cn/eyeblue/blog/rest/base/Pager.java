package cn.eyeblue.blog.rest.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;


@Data
@NoArgsConstructor
public class Pager<T> {

    //当前页码。
    private long page;


    private long pageSize;

    //总条目数
    private long totalItems;

    //总页数
    private long totalPages;

    private List<T> data;


    public Pager(long page, long pageSize, long totalItems, List<T> data) {

        this.page = page;

        this.pageSize = pageSize;

        this.totalItems = totalItems;

        this.totalPages = (long) Math.ceil(this.totalItems * 1.0 / this.pageSize);

        this.data = data;

    }



    public Pager(Pager pager) {

        this.page = pager.getPage();

        this.pageSize = pager.getPageSize();

        this.totalItems = pager.getTotalItems();

        this.totalPages = (long) Math.ceil(this.totalItems * 1.0 / this.pageSize);

    }

}
