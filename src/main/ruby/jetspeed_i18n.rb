#!ruby
require 'find'
require 'rexml/document'

@@ROOT_PATH = 'C:/Desarrollo/Projects/BKO-TSGO/Trunk/sca-install/src/main/install/deploy/jetspeed.war/WEB-INF/pages/'
@@LANGS = ['es']
# Constant to process the psml files , should be moddified if there is something more needed
EXCLUDES_DIRECTORIES = ['.svn']
TAGS = ['title','short-title','text']
TEXT = 'translate_this'
ACCEPTED_FILES_PATTERN=['*.psml','folder.metadata']

@files_processed     = 0
# Validates a xml string if it can be parsed
def valid_xml?(xml)
  create_document(xml) != nil
end

# Creates the document from a valid xml string
def create_document(xml)
  begin
    REXML::Document.new(xml)    
  rescue REXML::ParseException
    # nil
  end  
end

#Iterates recursively throug a root path in order to find desired files to be process
def process_directory(path)
  Find.find(path) do |a_path|  
    Find.prune if EXCLUDES_DIRECTORIES.include?(File.basename(a_path))
    if (ACCEPTED_FILES_PATTERN.each { | pattern| File.basename(pattern)} and
          !File.directory?(a_path))
      @files_processed += 1
      document = moddify_document(a_path)
      modify_file(a_path,document)
    end
  end
end

# Read the file and translate the document adding the i18n needed tags
def moddify_document(path)
  doc = nil
  File.open(path,'r+') do | file|
    xml_string = file.read
    doc = process_xml(xml_string) if valid_xml?(xml_string)
  end
  doc
end

# Write the xml moddified to the file
def modify_file(path,document)  
  File.open(path,'w') do|file|
     file.write(document) if document
  end
end

# Modiffies the xml String adding the metadata needed by the
# Jetspeed portal engine to support i18n on it's pages
def process_xml(string)
  xml_processor_helper(create_document(string))
end

# Helper method for recursion
def xml_processor_helper(document)
  document.each_element do |element|
    if TAGS.include?(element.name)
      @@LANGS.each do |lang| 
        temp_doc = create_document("<metadata name='#{element.name}' xml:lang='#{lang}'>#{TEXT}</metadata>\n")
        element.parent.add(temp_doc) unless element_exists(element.parent,element.name,lang)       
      end
    end
    process_child(element)
  end
  document
end

# Helper method for the indirect recursion
def process_child(element)
  if element.has_elements?
      xml_processor_helper(element)
   end
end

# valida si existe el elemento con el mismo lenguaje y el mismo
# elemento al que afecta
def element_exists(document,element_name,lang)
  xpath = REXML::XPath
  xpath.first(document, "//metadata[@name='#{element_name}'][@xml:lang='#{lang}']") != nil
end

def time_elapsed()
  time_in_milis = Time.new
  yield if block_given?
  time_in_milis = Time.new - time_in_milis
  puts "Executed in #{time_in_milis} ms"
end

# imprime como usarlo si no se le pasan parametros
if ARGV.size == 0
  puts "Para utilizarlo debe pasarle como primer argunmento el directorio en donde
se encuentra las paginas del portal ejemplo ('jetspeed/WEB-INF/pages') y a continuacion
una lista de idiomas ejemplo ('en fr de' , para ingles, frances aleman respectivamente)
separados por espacio y siguieno la especificacion de la i18n"

  puts "Ejemplo.
ruby jetspeed_i18n.rb c:/portal_pages en fr de"
  exit(0)
end
# Added the languajes to the collection in case the are needed and the path directory
n = 0
ARGV.each do |arg|
  if n == 0 
    @@ROOT_PATH = arg
  else
    @@LANGS << arg unless @@LANGS.include?(arg)
  end
  n += 1
end
# end argument parse

puts "Se inicio el proceso de modificacion de los archivos de portal"
puts "Se van a agregar los idiomas #{@@LANGS} a los folder.metadata y *.psml\
\n que se encuentren en el directorio #{@@ROOT_PATH}"

time_elapsed {process_directory(@@ROOT_PATH)}


puts "Se procesaron #{@files_processed}"

