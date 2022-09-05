package zerobase.weather.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JdbcMemoRepositoryTest {

    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
    void insertMemoTest() {
        //given
        Memo memo = new Memo(1, "this is new memo");

        // when
        jdbcMemoRepository.save(memo);

        //then
        Optional<Memo> result = jdbcMemoRepository.findById(1);
        assertEquals(result.get().getText(), "this is new memo");
    }

    @Test
    void findAllMemoTest() {
        //given

        // when
        List<Memo> memoList = jdbcMemoRepository.findAll();
        System.out.println(memoList);

        //then
        assertNotNull(memoList);
    }
}