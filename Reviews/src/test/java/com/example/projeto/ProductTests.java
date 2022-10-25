package com.example.projeto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTests {

    @Test
    void ensureDesignationMustNotBeBlank() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Product product = new Product("","gfdsfds","sku0000001");
            }
        });
    }

    @Test
    void ensureDesignationMustNotBeBlankSpaces() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Product product = new Product("     ","gfdsfds","sku0000001");
            }
        });
    }

    @Test
    public void ensureDesignationDoesntHaveMoreThan50Characters() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Product product = new Product("asdfg12345qwhsjdkchjksdklcjsadjwoiqu9wqu9ewjxkjcxzkljk","qwertyuiopasdfghjklçzxcvbnmqwertyuiopasdfghjklçzxcvbnm","sku0000001");
            }
        });
    }

    @Test
    void ensureDescriptionMustNotBeBlank() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Product product = new Product("banana","","sku0000001");
            }
        });
    }

    @Test
    void ensureDescriptionMustNotBeBlankSpaces() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Product product = new Product("banana","    ","sku0000001");
            }
        });
    }

    @Test
    void ensureDescriptionDoesntHaveMoreThan1000Characters () {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Product product = new Product("Folha", "asafdfdfdjskhjkdhjfkshjkcdhsjkchdjskhcklxzjcklxzjcklxzjclkxzjclkxjzlcjxzlkcjxklzhcjxzgvjkxzvjkxzhvklzxjvxzjvklxjzklvjxzlkvjxzlkjvçlxzjvçlxzjvçlxzjvçlxzjvçlxzjvçlxzjvlçzifuydastasgcjkcxzjçlcxjzkcjklxzjclkxzhvkzwedtasyudtuisaicxzopvixzpovxzkcçlxzvçºzvzvizxupovcxkzçcxzçlvjklxzhvklxzhvxzklhvizxklvcçlzxvçlxzkvçzkasafdfdfdjskhjkdhjfkshjkcdhsjkchdjskhcklxzjcklxzjcklxzjclkxzjclkxjzlcjxzlkcjxklzhcjxzgvjkxzvjkxzhvklzxjvxzjvklxjzklvjxzlkvjxzlkjvçlxzjvçlxzjvçlxzjvçlxzjvçlxzjvçlxzjvlçzifuydastasgcjkcxzjçlcxjzkcjklxzjclkxzhvkzwedtasyudtuisaicxzopvixzpovxzkcçlxzvçºzvzvizxupovcxkzçcxzçlvjklxzhvklxzhvxzklhvizxklvcçlzxvçlxzkvçzkasafdfdfdjskhjkdhjfkshjkcdhsjkchdjskhcklxzjcklxzjcklxzjclkxzjclkxjzlcjxzlkcjxklzhcjxzgvjkxzvjkxzhvklzxjvxzjvklxjzklvjxzlkvjxzlkjvçlxzjvçlxzjvçlxzjvçlxzjvçlxzjvçlxzjvlçzifuydastasgcjkcxzjçlcxjzkcjklxzjclkxzhvkzwedtasyudtuisaicxzopvixzpovxzkcçlxzvçºzvzvizxupovcxkzçcxzçlvjklxzhvklxzhvxzklhvizxklvcçlzxvçlxzkvçzkasafdfdfdjskhjkdhjfkshjkcdhsjkchhdjskhcklxzjcklxzjcklxzjclkxzjclkxjzlcjxzlkcjxkzlkjvçlxzjvçlxzjvçlxzçzk", "SKU0000001");
            }
        });
    }

    @Test
    void ensureSkuMustNotBeBlank() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Product product = new Product("banana","sfadsadsadxsa","");
        }
        });
    }


    @Test
    void ensureSkuMustNotBeBlankSpaces() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Product product = new Product("banana","sfadsadsadxsa","    ");
            }
        });
    }

    @Test
    void ensureSkuDoesntHaveLessThan10Characters() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Product product = new Product("Folha", "asafdfd", "SKU00001");
            }
        });
    }
        @Test
        void ensureSkuDoesntHaveMoreThan10Characters () {
            assertThrows(IllegalArgumentException.class, new Executable() {
                @Override
                public void execute() throws Throwable {
                    Product product = new Product("Folha", "asafdfd", "SKU000000000001");
                }
            });
        }
        @Test
        public void ensureSkuDoesntHaveOnlyLetters() {
            assertThrows(IllegalArgumentException.class, new Executable() {
                @Override
                public void execute() throws Throwable {
                    Product product = new Product("Folha","asafddf","skufghtrei");
            }
        });
    }
        @Test
        public void ensureSkuDoesntHaveOnlyNumbers() {
            assertThrows(IllegalArgumentException.class, new Executable() {
                @Override
                public void execute() throws Throwable {
                    Product product = new Product("Folha","asafddf","2564765765");
            }
        });
    }

    }
