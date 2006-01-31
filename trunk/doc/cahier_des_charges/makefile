LEX=pdflatex
LEXOPTIONS=-interaction nonstopmode
DIA=dia
EPSTOPDF=epstopdf
BIBTEX=bibtex
FIGURESPDF=classes.pdf

all: main.pdf

clean:
	rm -f *.pdf *.toc *.aux *.out *.log *.bbl *.blg *.eps

send: main.pdf
	scp main.pdf cassou@shell.berlios.de:/home/groups/visidia/htdocs/cahier_des_charges.pdf

main.pdf: main.aux main.tex main.bbl $(FIGURESPDF)
	$(LEX) $(LEXOPTIONS) main || echo
	$(LEX) $(LEXOPTIONS) main || echo

main.aux: main.tex $(FIGURESPDF)
	$(LEX) $(LEXOPTIONS) main || echo
	$(LEX) $(LEXOPTIONS) main || echo

main.bbl: main.tex myBib.bib
	$(BIBTEX) main

classes.eps: figures/classes.eps
	cp figures/classes.eps .

classes.pdf: classes.eps
	$(EPSTOPDF) classes.eps
