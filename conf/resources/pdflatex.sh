#!/usr/bin/bash

echo "s\n" | pdflatex -shell-escape book.tex > /dev/null || true
bibtex book || true
makeindex book.idx || true
echo "s\n" | pdflatex -shell-escape book.tex > /dev/null || true
echo "s\n" | pdflatex -shell-escape book.tex > /dev/null || true