#!/bin/bash
echo Gerando latex...

if [ ! -f `pwd`/answer.tex ]; then
    touch `pwd`/answer.tex;
fi

pdflatex -interaction nonstopmode -shell-escape book.tex > /dev/null
bibtex book
makeindex book.idx
pdflatex -interaction nonstopmode -shell-escape book.tex > /dev/null
pdflatex -interaction nonstopmode -shell-escape book.tex > /dev/null
echo Terminou :)