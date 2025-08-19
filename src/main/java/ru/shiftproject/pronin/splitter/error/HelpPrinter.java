package ru.shiftproject.pronin.splitter.error;

public class HelpPrinter {
    public static void printHelp() {
        System.out.print("""
                Инструкция по запуску:
                    В командной строке в папке с jar-файлом введите:
                        "java -jar file-separator-1.0.jar <набор опций и файлов>"
                    Доступные опции:
                        -o	путь к результату (-o /some/path)
                        -p	префикс к названию файла (-p result_)
                        -a	добавление в существующие файлы (по умолчанию перезаписываются)
                        -s	краткая статистика
                        -f	полная статистика
                    По умолчанию файлы с результатами располагаются в текущей папке с именами
                        integers.txt, floats.txt, strings.txt
                
                    Пример запуска утилиты:
                        java -jar file-separator-1.0.jar -o res -s -a -p result_ in1.txt in2.txt
                
                    Подробную инструкцию см. в файле "help.txt"
                """);
    }
}