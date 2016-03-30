package cn.marco.meizhi.domain;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

@Table("Result")
public class Result {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column("_id")
    public long id;

    @Column("desc")
    public String desc;

    @Column("publishedAt")
    public String publishedAt;

    @Column("url")
    public String url;

    @Column("who")
    public String who;

    @Column("type")
    public String type;

    @Column("category")
    public String category;

}
