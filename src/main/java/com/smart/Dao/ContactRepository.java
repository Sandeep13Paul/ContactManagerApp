package com.smart.Dao;

import com.smart.Models.Contact;
import com.smart.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository  extends JpaRepository<Contact, Integer> {

//    @Query("from Contact as c where c.user.id = :userId")
//    public List<Contact> findContactsByUser(@Param("userId") int userId);

    //pagination
    //pageable-currentPage = page, Contact per page(5)
    //
    @Query("from Contact as c where c.user.id = :userId")
    public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);

    //get contact

    @Query("from Contact as c where c.cid = :cid")
    public Contact getContactByContactID(int cid);

    @Query("from Contact as c where c.cname LIKE %:name% and c.user = :user")
    public List<Contact> findByNameContainingAndUser(@Param("name") String keyword, @Param("user") User user);
}
