LEX=pdflatex
LEXOPTIONS=-interaction nonstopmode
DIA=dia
EPSTOPDF=epstopdf
BIBTEX=bibtex
FIGURESPDF=classes.pdf simple.pdf UseCase.pdf

SOURCES=main.tex introduction.tex modele_systeme.tex besoins.tex \
	organisation.tex lexique.tex biblio.tex desc_choisirAgent.tex \
        desc_choisirGraphe.tex desc_RegleEcriture.tex

all: main.pdf

clean:
	rm -f *.pdf *.toc *.aux *.out *.log *.bbl *.blg *.eps

send: main.pdf
	scp main.pdf cassou@shell.berlios.de:/home/groups/visidia/htdocs/cahier_des_charges.pdf

main.pdf: main.aux $(SOURCES) main.bbl $(FIGURESPDF)
	$(LEX) $(LEXOPTIONS) main || echo
	$(LEX) $(LEXOPTIONS) main || echo

main.aux: $(SOURCES) $(FIGURESPDF)
	$(LEX) $(LEXOPTIONS) main || echo

main.bbl: $(SOURCES) myBib.bib
	$(BIBTEX) main

simple.eps: figures/simple.eps
	cp figures/simple.eps .

simple.pdf: simple.eps
	$(EPSTOPDF) simple.eps

UseCase.eps: figures/UseCase.eps
	cp figures/UseCase.eps .

UseCase.pdf: UseCase.eps
	$(EPSTOPDF) UseCase.eps

classes.eps: figures/classes.eps
	cp figures/classes.eps .

classes.pdf: classes.eps
	$(EPSTOPDF) classes.eps
