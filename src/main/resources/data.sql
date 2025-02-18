INSERT INTO roles (roleid, rolename) VALUES
                                         (1, 'MODERATOR'),
                                         (2, 'BUSINESS'),
                                         (3, 'EMPLOYEE'),
                                         (4, 'USER')
    ON CONFLICT (roleid) DO NOTHING;
