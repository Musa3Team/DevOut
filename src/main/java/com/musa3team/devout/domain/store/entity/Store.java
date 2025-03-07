package com.musa3team.devout.domain.store.entity;

import com.musa3team.devout.common.constants.StoreCategory;
import com.musa3team.devout.common.constants.StoreStatus;
import com.musa3team.devout.common.entity.BaseEntity;
import com.musa3team.devout.domain.member.entity.Member;
import com.musa3team.devout.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.musa3team.devout.domain.store.valid.TenMinuteIntervalValidator.isValidTenMinuteInterval;

@Entity
@Getter
@NoArgsConstructor
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String telephoneNumber;

    private String address;

    private String contents;

    private String name;

    private LocalTime openTime;

    private LocalTime closeTime;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    private int minimumPrice;

    @Enumerated(EnumType.STRING)
    private StoreCategory category;

    @OneToMany(mappedBy = "store")
    private List<Menu> menus = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Store(String telephoneNumber, String address, String contents, String name, LocalTime openTime, LocalTime closeTime, int minimumPrice, StoreCategory category, Member member) {
        this.telephoneNumber = telephoneNumber;
        this.address = address;
        this.contents = contents;
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minimumPrice = minimumPrice;
        this.category = category;
        this.member = member;
    }

    public void uddateStatus(StoreStatus status) {
        this.status = status;
    }

    public void updateStore(StoreStatus status,String address, String telephoneNumber, StoreCategory category, String name, String contents, int minimumPrice, LocalTime openTime, LocalTime closeTime) {
        if(status.equals(StoreStatus.SHUTDOWN))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "폐업한 가게입니다.");

        if (address != null && !address.isBlank()) this.address = address;

        if (category != null) this.category = category;

        if (name != null && !name.isBlank()) this.name = name;

        if (contents != null && !contents.isBlank()) this.contents = contents;

        if (minimumPrice > 0) this.minimumPrice = minimumPrice;

        if (telephoneNumber != null && !telephoneNumber.isBlank()) {
            if (Pattern.matches("^(02|0[3-6][1-5])-?\\d{3,4}-?\\d{4}$", telephoneNumber))
                this.telephoneNumber = telephoneNumber;
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "전화번호 형식을 확인하세요");
        }

        if (openTime != null) {
            if (isValidTenMinuteInterval(openTime)) this.openTime = openTime;
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "분은 10분 단위만 입력 가능합니다.");
        }

        if (closeTime != null) {
            if (isValidTenMinuteInterval(closeTime)) this.closeTime = closeTime;
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "분은 10분 단위만 입력 가능합니다.");
        }
    }

    public void setId(long l) {
    }
}
