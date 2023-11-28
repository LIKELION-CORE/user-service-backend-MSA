INSERT INTO MEMBER (name, user_id, password, phone, state, member_role, student_Id, department)
SELECT '리진아', 'admin@naver.com', '$2a$10$a9PdE5Bj0UxB4DxwUNuH/.WclddN/81gI9AN3pMdnmmrEnzenNX6C', '010-1234-1234', true, 'ROOT', '12341234', 'ICT공학부'
    WHERE NOT EXISTS (SELECT 1 FROM MEMBER WHERE user_id = 'admin@naver.com');
